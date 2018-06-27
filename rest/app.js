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

/**
 * Gets Rank from name
 *
 * 404 if rank name is not found
 */
app.get('/rank/:name', (req, res) => {
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
app.post('/rank', urlencoder, (req, res) => {
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
 * Removes a rank from the database
 *
 * 200 Rank has been deleted
 * 404 Rank does not exist
 * 400 Invalid Request missing rank name
 */
app.delete('/rank', urlencoder, (req, res) => {
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
app.put('/rank', urlencoder, (req, res) => {
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

// Start NodeJS Server
app.listen(port, () => {
    console.log("Starting up Server-Essentials Rest API using port '" + port + "'");
});
