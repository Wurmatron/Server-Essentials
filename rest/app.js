// Config
const port = 8080;
const ranksDir = './ranks';
const userDir = './users';

// Require / Imports
const express = require('express');
const morgan = require('morgan');
const levelup = require('levelup');
const leveldown = require('leveldown');
const bodyParser = require('body-parser');

// Setup
const app = express();
const ranksDB = levelup(leveldown(ranksDir));
const userDB = levelup(leveldown(userDir));
const urlencoder = bodyParser.urlencoded({
    extended: true
});
app.use(bodyParser.json());
app.use(morgan('short'));
app.set('json spaces', 2);

/**
 * Gets Rank from name
 *
 * 404 if rank name is not found
 */
app.get('/rank/find/:name', (req, res) => {
    const rank = ranksDB.get(req.params.name);
    rank.then(function (result) {
        res.json(JSON.parse(result.toString('utf8')))
    }, function (err) {
        res.sendStatus(404);
    })
});

/**
 * Creates a new rank entry from input json data
 *
 * 409 Rank Already exists
 * 201 Created
 * 400 Invalid Request missing rank name
 */
app.post('/rank/add', urlencoder, (req, res) => {
    if (req.body.name) {
        const rank = ranksDB.get(req.body.name)
        rank.then(function (result) {
            if (!rank) {
                addRankEntry(req, res)
            } else {
                res.sendStatus(409)
            }
        }, function (err) {
            addRankEntry(req, res)
        })
    }
});

/**
 * Returns a Array of all the ranks
 *
 * 304 Ranks sent
 */
app.get('/rank/find', urlencoder, (req, res) => {
    var allRanks = []
    ranksDB.createReadStream()
        .on('data', function (data) {
            const rankData = JSON.parse(data.value.toString('utf8'))
            console.log(rankData)
            allRanks.push({
                name: rankData.name,
                prefix: rankData.prefix,
                suffix: rankData.suffix,
                inheritance: rankData.inheritance,
                permission: rankData.permission
            })
        })
        .on('error', function (err) {
            console.log('Oh my!', err)
        })
        .on('close', function () {})
        .on('end', function () {
            res.json(allRanks)
        })
})

/**
 * Removes a rank from the database
 *
 * 200 Rank has been deleted
 * 404 Rank does not exist
 * 400 Invalid Request missing rank name
 */
app.delete('/rank/delete', urlencoder, (req, res) => {
    if (req.body.name) {
        const rank = ranksDB.get(req.body.name)
        rank.then(function (result) {
            console.log("Removing entry '" + req.body.name + "'");
            ranksDB.del(req.body.name);
            res.sendStatus(200)
        }, function (err) {
            res.sendStatus(404)
        })
    } else {
        res.sendStatus(400)
    }
});

/**
 * Overrides a current rank or created a new one
 *
 * 400 Invalid Request missing rank name
 * 201 Rank Overridden / Created
 */
app.put('/rank/override', urlencoder, (req, res) => {
    if (req.body.name) {
        addRankEntry(req, res)
    } else {
        res.sendStatus(400)
    }
});

/**
 * Add an entry to the rank DataBase
 *
 * 201 if rank is added
 * 400 Invalid request, missing rank name
 */
function addRankEntry(req, res) {
    console.log("Adding entry '" + req.body.name + "'");
    ranksDB.put(req.body.name, JSON.stringify({
        name: req.body.name,
        prefix: req.body.prefix,
        suffix: req.body.suffix,
        inheritance: req.body.inheritance,
        permission: req.body.permission
    }), function (err) {
        if (err)
            req.sendStatus(400);
    });
    res.sendStatus(201);
}

// =-=-=-=-=-=-=-=-=-=-=-=-=
//        User
// =-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * Gets User from uuid
 *
 * 404 if uuid / player is not found
 */
app.get('/user/find/:uuid', (req, res) => {
    const user =  userDB.get(req.params.uuid);
    user.then(function (result) {
        res.json(JSON.parse(result.toString('utf8')))
    }, function (err) {
        res.sendStatus(404);
    })
});

/**
 * Creates a new user entry from input json data
 *
 * 409 User already exists
 * 201 Created
 * 400 Invalid Request missing uuid
 */
app.post('/user/add', urlencoder, (req, res) => {
    if (req.body.uuid) {
        const user = userDB.get(req.body.uuid)
        user.then(function (result) {
            if (!user) {
                addUserEntry(req, res)
            } else {
                res.sendStatus(409)
            }
        }, function (err) {
            addUserEntry(req, res)
        })
    } else {
        res.sendStatus(400)
    }
});

/**
 * Returns a Array of all the users
 *
 * 304 User sent
 */
app.get('/user/find', urlencoder, (req, res) => {
    var allUsers = []
    userDB.createReadStream()
        .on('data', function (data) {
            const userData = JSON.parse(data.value.toString('utf8'))
            console.log(userData)
            allUsers.push({
                uuid: userData.uuid,
                discord: userData.discord,
            })
        })
        .on('error', function (err) {
            console.log('Oh my!', err)
        })
        .on('close', function () {
        })
        .on('end', function () {
            res.json(allUsers)
        })
})

/**
 * Removes a user from the database
 *
 * 200 User has been deleted
 * 404 User does not exist
 * 400 Invalid Request missing user
 */
app.delete('/user/delete', urlencoder, (req, res) => {
    if (req.body.uuid) {
        const user = userDB.get(req.body.uuid)
        user.then(function (result) {
            console.log("Removing entry '" + req.body.uuid + "'");
            userDB.del(req.body.uuid);
            res.sendStatus(200)
        }, function (err) {
            res.sendStatus(404)
        })
    } else {
        res.sendStatus(400)
    }
});

/**
 * Overrides a current user or created a new one
 *
 * 400 Invalid Request missing rank name
 * 201 User Overridden / Created
 */
app.put('/user/override', urlencoder, (req, res) => {
    if (req.body.uuid) {
        addUserEntry(req, res)
    } else {
        res.sendStatus(400)
    }
});

/**
 * Add an entry to the user DataBase
 *
 * 201 if user is added
 * 400 Invalid request, missing user uuid
 */
function addUserEntry(req, res) {
    console.log("Adding entry '" + req.body.uuid + "'");
    userDB.put(req.body.uuid, JSON.stringify({
        uuid: req.body.uuid,
        discord: req.body.discord,
        rank: req.body.rank,
        permission: req.body.permission
    }), function (err) {
        if (err)
            req.sendStatus(400);
    });
    res.sendStatus(201);
}


// Start NodeJS Server
app.listen(port, () => {
    console.log("Starting up Server-Essentials Rest API using port '" + port + "'");
});
