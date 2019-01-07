const levelup = require("levelup");
const leveldown = require("leveldown");
const userDB = levelup(leveldown("./users"));
const apiKeys = require("fs").readFileSync("keys").toString();

module.exports = {
    add: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.body.uuid) {
                const user = userDB.get(req.body.uuid);
                user.then(function (result) {
                    if (!user) {
                        addUserEntry(req, res, false)
                    } else {
                        res.sendStatus(409)
                    }
                }, function (err) {
                    addUserEntry(req, res, false)
                })
            } else {
                res.sendStatus(400)
            }
        } else {
            res.sendStatus(401)
        }
    },

    find: async (req, res, next) => {
        const user = userDB.get(req.params.uuid);
        user.then(function (result) {
            res.json(JSON.parse(result.toString('utf8')))
        }, function (err) {
            res.sendStatus(404);
        })
    },

    findAll: async (req, res, next) => {
        var allUsers = [];
        userDB.createReadStream()
            .on('data', function (data) {
                const userData = JSON.parse(data.value.toString('utf8'));
                allUsers.push({
                    uuid: userData.uuid,
                    discord: userData.discord,
                    rank: userData.rank
                })
            })
            .on('error', function (err) {
                console.log('Error!, ', err);
            })
            .on('close', function () {
            })
            .on('end', function () {
                res.json(allUsers)
            })
    },

    findEco: async (req, res, next) => {
        var allUsers = [];
        userDB.createReadStream()
            .on('data', function (data) {
                const userData = JSON.parse(data.value.toString('utf8'));
                allUsers.push({
                    uuid: userData.uuid,
                    bank: userData.bank
                })
            })
            .on('error', function (err) {
                console.log('Error!, ', err);
            })
            .on('close', function () {
            })
            .on('end', function () {
                res.json(allUsers)
            })
    },

    delete: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.params.uuid) {
                const user = userDB.get(req.params.uuid);
                user.then(function (result) {
                    console.log("Removing User '" + req.body.uuid + "'");
                    userDB.del(req.body.uuid);
                    res.sendStatus(200)
                }, function (err) {
                    res.sendStatus(404)
                })
            } else {
                res.sendStatus(400)
            }
        } else
            res.sendStatus(401)
    },

    override: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.params.uuid) {
                addUserEntry(req, res)
            } else {
                res.sendStatus(400)
            }
        } else
            res.sendStatus(401)
    }
};

function addUserEntry(req, res, override) {
    if (!override) {
        console.log("Adding User '" + req.body.uuid + "'");
        userDB.put(req.body.uuid, JSON.stringify({
            uuid: req.body.uuid,
            rank: req.body.rank,
            nick: req.body.nick,
            bank: req.body.bank,
            team: req.body.team,
            onlineTime: req.body.onlineTime,
            lastSeen: req.body.lastSeen,
            stocks: req.body.stocks,
            loadedChunks: req.body.loadedChunks,
            firstJoin: req.body.firstJoin,
            muted: req.body.muted,
            lang: req.body.lang,
            discord: req.body.discord,
            permission: req.body.permission,
            perks: req.body.perks
        }), function (err) {
            if (err)
                res.req.sendStatus(400);
        });
    }
    res.sendStatus(201);
}