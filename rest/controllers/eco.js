const levelup = require("levelup");
const leveldown = require("leveldown");
const ecoDB = levelup(leveldown("./eco"));
const apiKeys = require("fs").readFileSync("keys").toString();

module.exports = {
    add: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.body.name) {
                const eco = ecoDB.get(req.body.name);
                eco.then(function (result) {
                    if (!eco) {
                        addEcoEntry(req, res, false)
                    } else {
                        res.sendStatus(409)
                    }
                }, function (err) {
                    addEcoEntry(req, res, false)
                })
            } else {
                res.sendStatus(400)
            }
        } else {
            res.sendStatus(401)
        }
    },

    find: async (req, res, next) => {
        const eco = ecoDB.get(req.params.name);
        eco.then(function (result) {
            res.json(JSON.parse(result.toString('utf8')))
        }, function (err) {
            res.sendStatus(404);
        })
    },

    findAll: async (req, res, next) => {
        let autoecos = [];
        ecoDB.createReadStream()
            .on('data', function (data) {
                const ecoData = JSON.parse(data.value.toString('utf8'));
                autoecos.push({
                    name: ecoData.name
                })
            })
            .on('error', function (err) {
                console.log('Error!, ', err);
            })
            .on('close', function () {
            })
            .on('end', function () {
                res.json(autoecos)
            })
    },

    delete: async (req, res, next) => {
        if (apiKeys.indexOf(req.get("authKey")) > -1) {
            if (req.params.name) {
                const eco = ecoDB.get(req.params.name);
                eco.then(function (result) {
                    console.log("Removing eco '" + req.body.name + "'");
                    ecoDB.del(req.body.name);
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
                addEcoEntry(req, res)
            } else {
                res.sendStatus(400)
            }
        } else
            res.sendStatus(401)
    }
};

function addEcoEntry(req, res) {
    console.log("Adding Currency '" + req.body.name + "'");
    ecoDB.put(req.body.name, JSON.stringify({
        name: req.body.name,
        sell: req.body.sell,
        buy: req.body.buy,
    }), function (err) {
        if (err)
            res.req.sendStatus(400);
    });
    res.sendStatus(201);
}
