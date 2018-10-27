const levelup = require("levelup");
const leveldown = require("leveldown");
const rankDB = levelup(leveldown("./ranks"));
const apiKeys = require("fs").readFileSync("keys").toString();

module.exports = {
    add: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.body.name) {
                const rank = rankDB.get(req.body.name);
                rank.then(function (result) {
                    if (!rank) {
                        addRankEntry(req, res, false)
                    } else {
                        res.sendStatus(409)
                    }
                }, function (err) {
                    addRankEntry(req, res, false)
                })
            } else {
                res.sendStatus(400)
            }
        } else {
            res.sendStatus(401)
        }
    },

    find: async (req, res, next) => {
        const rank = rankDB.get(req.params.name);
        rank.then(function (result) {
            res.json(JSON.parse(result.toString('utf8')))
        }, function (err) {
            res.sendStatus(404);
        })
    },

    findAll: async (req, res, next) => {
        let allRanks = [];
        rankDB.createReadStream()
            .on('data', function (data) {
                const rankData = JSON.parse(data.value.toString('utf8'));
                allRanks.push({
                    name: rankData.name,
                    prefix: rankData.prefix,
                    suffix: rankData.suffix,
                    inheritance: rankData.inheritance,
                    permission: rankData.permission
                })
            })
            .on('error', function (err) {
                console.log('Error!, ', err);
            })
            .on('close', function () {
            })
            .on('end', function () {
                res.json(allRanks)
            })
    },

    delete: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.params.name) {
                const rank = rankDB.get(req.params.name);
                rank.then(function (result) {
                    console.log("Removing Rank '" + req.body.name + "'");
                    rankDB.del(req.body.name);
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
                addRankEntry(req, res)
            } else {
                res.sendStatus(400)
            }
        } else
            res.sendStatus(401)
    }
};

function addRankEntry(req, res) {
    rankDB.put(req.body.name, JSON.stringify({
        name: req.body.name,
        prefix: req.body.prefix,
        suffix: req.body.suffix,
        inheritance: req.body.inheritance,
        permission: req.body.permission
    }), function (err) {
        if (err)
            res.req.sendStatus(400);
    });
    res.sendStatus(201);
}