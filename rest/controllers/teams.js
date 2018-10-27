const levelup = require("levelup");
const leveldown = require("leveldown");
const teamDB = levelup(leveldown("./teams"));
const apiKeys = require("fs").readFileSync("keys").toString();

module.exports = {
    add: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.body.name) {
                const team = teamDB.get(req.body.name);
                team.then(function (result) {
                    if (!team) {
                        addTeamEntry(req, res, false)
                    } else {
                        res.sendStatus(409)
                    }
                }, function (err) {
                    addTeamEntry(req, res, false)
                })
            } else {
                res.sendStatus(400)
            }
        } else {
            res.sendStatus(401)
        }
    },

    find: async (req, res, next) => {
        const team = teamDB.get(req.params.name);
        team.then(function (result) {
            res.json(JSON.parse(result.toString('utf8')))
        }, function (err) {
            res.sendStatus(404);
        })
    },

    findAll: async (req, res, next) => {
        let autoteams = [];
        teamDB.createReadStream()
            .on('data', function (data) {
                const teamData = JSON.parse(data.value.toString('utf8'));
                autoteams.push({
                    name: teamData.name
                })
            })
            .on('error', function (err) {
                console.log('Error!, ', err);
            })
            .on('close', function () {
            })
            .on('end', function () {
                res.json(autoteams)
            })
    },

    delete: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.params.name) {
                const team = teamDB.get(req.params.name);
                team.then(function (result) {
                    console.log("Removing team '" + req.body.name + "'");
                    teamDB.del(req.body.name);
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
                addTeamEntry(req, res)
            } else {
                res.sendStatus(400)
            }
        } else
            res.sendStatus(401)
    }
};

function addTeamEntry(req, res, override) {
    if (!override) {
        console.log("Adding Team '" + req.body.name + "'");
    }
    teamDB.put(req.body.name, JSON.stringify({
        name: req.body.name,
        bank: req.body.bank,
        perks: req.body.perks,
        owner: req.body.owner,
        members: req.body.members
    }), function (err) {
        if (err)
            res.req.sendStatus(400);
    });
    res.sendStatus(201);
}