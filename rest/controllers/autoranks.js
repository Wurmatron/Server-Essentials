const levelup = require("levelup");
const leveldown = require("leveldown");
const autoRanksDB = levelup(leveldown("./autoRanks"));
const apiKeys = require("fs").readFileSync("keys").toString();

module.exports = {
    add: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.body.name) {
                const rank = autoRanksDB.get(req.body.name);
                rank.then(function (result) {
                    if (!rank) {
                        addAutoRankEntry(req, res, false)
                    } else {
                        res.sendStatus(409)
                    }
                }, function (err) {
                    addAutoRankEntry(req, res, false)
                })
            } else {
                res.sendStatus(400)
            }
        } else {
            res.sendStatus(401)
        }
    },

    find: async (req, res, next) => {
        const rank = autoRanksDB.get(req.params.name);
        rank.then(function (result) {
            res.json(JSON.parse(result.toString('utf8')))
        }, function (err) {
            res.sendStatus(404);
        })
    },

    findAll: async (req, res, next) => {
        let autoRanks = [];
        autoRanksDB.createReadStream()
            .on('data', function (data) {
                const rankData = JSON.parse(data.value.toString('utf8'));
                autoRanks.push({
                    name: rankData.name
                })
            })
            .on('error', function (err) {
                console.log('Error!, ', err);
            })
            .on('close', function () {
            })
            .on('end', function () {
                res.json(autoRanks)
            })
    },

    delete: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.params.name) {
                const rank = autoRanksDB.get(req.params.name);
                rank.then(function (result) {
                    console.log("Removing Rank '" + req.body.name + "'");
                    autoRanksDB.del(req.body.name);
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
            if (req.params.name) {
                addAutoRankEntry(req, res)
            } else {
                res.sendStatus(400)
            }
        } else
            res.sendStatus(401)
    }
};

function addAutoRankEntry(req, res, override) {
    if (!override) {
        console.log("Adding AutoRank '" + req.body.name + "'");
    }
    autorankDB.put(req.body.nextRank, JSON.stringify({
        playTime: req.body.playTime,
        balance: req.body.balance,
        exp: req.body.exp,
        rank: req.body.rank,
        nextRank: req.body.nextRank
    }), function (err) {
        if (err)
            req.sendStatus(400);
    });
    res.sendStatus(201);
}