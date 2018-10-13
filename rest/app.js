// Config
const port = 8080;
const ranksDir = "./ranks";
const userDir = "./users";
const teamDir = "./teams";
const autorankDir = "./autorank";
const apiKey = "keys";
const currentEconomy = "./economy";

// Require / Imports
const express = require("express");
const morgan = require("morgan");
const levelup = require("levelup");
const leveldown = require("leveldown");
const bodyParser = require("body-parser");

// Setup
const app = express();
const ranksDB = levelup(leveldown(ranksDir));
const userDB = levelup(leveldown(userDir));
const teamDB = levelup(leveldown(teamDir));
const autorankDB = levelup(leveldown(autorankDir));
const ecoDB = levelup(leveldown(currentEconomy));
const urlencoder = bodyParser.urlencoded({
    extended: true
});
app.use(bodyParser.json());
app.use(morgan('short'));
app.set('json spaces', 2);
var apiKeys = require("fs").readFileSync(apiKey).toString();

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
      res.req.sendStatus(400);
  });
  res.sendStatus(201);
}

/**
 * Gets Rank from name
 *
 * 404 if rank name is not found
 */
app.get("/rank/find/:name", (req, res) => {
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
app.post("/rank/add", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            const rank = ranksDB.get(req.body.name)
            rank.then(function (result) {
                if (!rank) {
                    addRankEntry(req, res);
                } else {
                    res.sendStatus(409)
                }
            }, function (err) {
                addRankEntry(req, res)
            })
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Returns a Array of all the ranks
 *
 * 304 Ranks sent
 */
app.get("/rank/find", urlencoder, (req, res) => {
    var allRanks = []
    ranksDB.createReadStream()
        .on('data', function (data) {
            const rankData = JSON.parse(data.value.toString('utf8'))
            allRanks.push({
                name: rankData.name,
                prefix: rankData.prefix,
                suffix: rankData.suffix,
                inheritance: rankData.inheritance,
                permission: rankData.permission
            })
        })
        .on('error', function (err) {
            console.log('Error, ', err);
        })
        .on('close', function () {
        })
        .on('end', function () {
            res.json(allRanks);
        })
})

/**
 * Removes a rank from the database
 *
 * 200 Rank has been deleted
 * 404 Rank does not exist
 * 400 Invalid Request missing rank name
 */
app.delete("/rank/delete", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
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
    } else {
        res.sendStatus(401)
    }
});

/**
 * Overrides a current rank or created a new one
 *
 * 400 Invalid Request missing rank name
 * 201 Rank Overridden / Created
 */
app.put("/rank/override", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            addRankEntry(req, res)
        } else {
            res.sendStatus(400)
        }
    } else {
        res.sendStatus(401)
    }
});

// =-=-=-=-=-=-=-=-=-=-=-=-=
//        User
// =-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * Gets User from uuid
 *
 * 404 if uuid / player is not found
 */
app.get("/user/find/:uuid", (req, res) => {
    const user = userDB.get(req.params.uuid);
    user.then(function (result) {
        res.json(JSON.parse(result.toString('utf8')))
    }, function (err) {
        res.sendStatus(404);
    })
})
;

/**
 * Creates a new user entry from input json data
 *
 * 409 User already exists
 * 201 Created
 * 400 Invalid Request missing uuid
 */
app.post("/user/add", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.uuid) {
            const user = userDB.get(req.body.uuid)
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
});

/**
 * Returns a Array of all the users
 *
 * 304 User sent
 */
app.get("/user/find", urlencoder, (req, res) => {
    var allUsers = []
    userDB.createReadStream()
        .on('data', function (data) {
            const userData = JSON.parse(data.value.toString('utf8'))
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
})

/**
 * Removes a user from the database
 *
 * 200 User has been deleted
 * 404 User does not exist
 * 400 Invalid Request missing user
 */
app.delete("/user/delete", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.uuid) {
            const user = userDB.get(req.body.uuid)
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
});

/**
 * Overrides a current user or created a new one
 *
 * 400 Invalid Request missing rank name
 * 201 User Overridden / Created
 */
app.put("/user/override", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.uuid) {
            addUserEntry(req, res, true)
        } else {
            res.sendStatus(400)
        }
    } else
        res.sendStatus(401)
});

/**
 * Add an entry to the user DataBase
 *
 * 201 if user is added
 * 400 Invalid request, missing user uuid
 */
function addUserEntry(req, res, override) {
    if (!override) {
        console.log("Adding User '" + req.body.uuid + "'");
    }
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
    res.sendStatus(201);
}

// =-=-=-=-=-=-=-=-=-=-=-=-=
//        Team
// =-=-=-=-=-=-=-=-=-=-=-=-=
/**
 * Gets Team from name
 *
 * 404 if team name is not found
 */
app.get("/team/find/:name", (req, res) => {
    const team = teamDB.get(req.params.name);
    team.then(function (result) {
        res.json(JSON.parse(result.toString('utf8')))
    }, function (err) {
        res.sendStatus(404);
    })
})
;

/**
 * Creates a new team entry from input json data
 *
 * 409 User already exists
 * 201 Created
 * 400 Invalid Request missing uuid
 */
app.post("/team/add", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            const team = teamDB.get(req.body.name)
            team.then(function (result) {
                if (!user) {
                    addUserEntry(req, res, false)
                } else {
                    res.sendStatus(409)
                }
            }, function (err) {
                addTeamEntry(req, res, false)
            })
        } else {
            res.sendStatus(400)
        }
    } else
        res.sendStatus(401)
});

/**
 * Returns a Array of all the teams
 *
 * 304 Team sent
 */
app.get("/team/find", urlencoder, (req, res) => {
    var allTeams = []
    teamDB.createReadStream()
        .on('data', function (data) {
            const teamData = JSON.parse(data.value.toString('utf8'))
            allTeams.push({
                name: teamData.name,
                owner: teamData.owner,
            })
        })
        .on('error', function (err) {
            console.log('Error! ', err)
        })
        .on('close', function () {
        })
        .on('end', function () {
            res.json(allTeams)
        })
})

/**
 * Removes a team from the database
 *
 * 200 Team has been deleted
 * 404 Team does not exist
 * 400 Invalid Request missing team name
 */
app.delete("/team/delete", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            const team = teamDB.get(req.body.name)
            team.then(function (result) {
                console.log("Removing Team '" + req.body.name + "'");
                teamDB.del(req.body.uuid);
                res.sendStatus(200)
            }, function (err) {
                res.sendStatus(404)
            })
        } else {
            res.sendStatus(400)
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Overrides a current team or created a new one
 *
 * 400 Invalid Request Invalid Team name
 * 201 Team Overridden / Created
 */
app.put("/team/override", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            addTeamEntry(req, res, true)
        } else {
            res.sendStatus(400)
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Add an entry to the Team DataBase
 *
 * 201 if team is added
 * 400 Invalid request, invalid / missing team name
 */
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

// =-=-=-=-=-=-=-=-=-=-=-=-=
//       Auto Rank
// =-=-=-=-=-=-=-=-=-=-=-=-=


/**
 * Creates a new AutoRank entry from input json data
 *
 * 409 AutoRank Already exists
 * 201 Created
 * 400 Invalid Request missing Autorank nextRank
 */
app.post("/autorank/add", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.nextRank) {
            const rank = autorankDB.get(req.body.nextRank)
            rank.then(function (result) {
                if (!rank) {
                    addAutoRankEntry(req, res, false)
                } else {
                    res.sendStatus(409)
                }
            }, function (err) {
                addAutoRankEntry(req, res, false)
            })
        }
    } else
        req.sendStatus(401)
});

/**
 * Returns a Array of all the AutoRanks
 *
 * 304 AutoRanks sent
 */
app.get("/autorank/find", urlencoder, (req, res) => {
    var allRanks = []
    autorankDB.createReadStream()
        .on('data', function (data) {
            const rankData = JSON.parse(data.value.toString('utf8'))
            allRanks.push({
                playTime: rankData.playTime,
                balance: rankData.balance,
                exp: rankData.exp,
                rank: rankData.rank,
                nextRank: rankData.nextRank
            })
        })
        .on('error', function (err) {
            console.log('Error, ', err)
        })
        .on('close', function () {
        })
        .on('end', function () {
            res.json(allRanks)
        })
})

/**
 * Removes a AutoRank from the database
 *
 * 200 AutoRank has been deleted
 * 404 AutoRank does not exist
 * 400 Invalid Request missing nextRank
 */
app.delete("/autorank/delete", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.nextRank) {
            const rank = autorankDB.get(req.body.nextRank)
            rank.then(function (result) {
                console.log("Removing AutoRank '" + req.body.nextRank + "'");
                autorankDB.del(req.body.nextRank);
                res.sendStatus(200)
            }, function (err) {
                res.sendStatus(404)
            })
        } else {
            res.sendStatus(400)
        }
    } else {
        req.sendStatus(401)
    }

});

/**
 * Overrides a current Autorank or created a new one
 *
 * 400 Invalid Request missing nextRank
 * 201 AutoRank Overridden / Created
 */
app.put("/autorank/override", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.nextRank) {
            addAutoRankEntry(req, res, true)
        } else {
            res.sendStatus(400)
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Add an entry to the rank DataBase
 *
 * 201 if rank is added
 * 400 Invalid request, missing rank name
 */
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

/**
 * Economy (Currency)
 */

/**
 * Gets Eco from name
 *
 * 404 if rank name is not found
 */
app.get("/eco/find/:name", (req, res) => {
    const eco = ecoDB.get(req.params.name);
    eco.then(function (result) {
        res.json(JSON.parse(result.toString('utf8')))
    }, function (err) {
        res.sendStatus(404);
    })
});

/**
 * Creates a new rank entry from input json data
 *
 * 409 Eco Already exists
 * 201 Created
 * 400 Invalid Request missing rank name
 */
app.post("/eco/add", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            const rank = ecoDB.get(req.body.name)
            rank.then(function (result) {
                if (!rank) {
                    addEcoEntry(req, res)
                } else {
                    res.sendStatus(409)
                }
            }, function (err) {
                addEcoEntry(req, res)
            })
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Returns a Array of all the currency's
 *
 * 304 Ecos sent
 */
app.get("/eco/find", urlencoder, (req, res) => {
    var allEcos = []
    ecoDB.createReadStream()
        .on('data', function (data) {
            const ecoData = JSON.parse(data.value.toString('utf8'))
            allEcos.push({
                name: ecoData.name,
                sell: ecoData.sell,
                buy: ecoData.buy,
            })
        })
        .on('error', function (err) {
            console.log('Error, ', err)
        })
        .on('close', function () {
        })
        .on('end', function () {
            res.json(allEcos)
        })
})

/**
 * Removes a eco from the database
 *
 * 200 Eco has been deleted
 * 404 Eco does not exist
 * 400 Invalid Request missing eco name
 */
app.delete("/eco/delete", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            const eco = ecoDB.get(req.body.name)
            eco.then(function (result) {
                console.log("Removing Currency '" + req.body.name + "'");
                ecoDB.del(req.body.name);
                res.sendStatus(200)
            }, function (err) {
                res.sendStatus(404)
            })
        } else {
            res.sendStatus(400)
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Overrides a current eco or created a new one
 *
 * 400 Invalid Request missing eco name
 * 201 Eco Overridden / Created
 */
app.put("/eco/override", urlencoder, (req, res) => {
    if (apiKeys.indexOf(req.body.authKey) > -1) {
        if (req.body.name) {
            addEcoEntry(req, res)
        } else {
            res.sendStatus(400)
        }
    } else {
        res.sendStatus(401)
    }
});

/**
 * Add an entry to the eco DataBase
 *
 * 201 if eco is added
 * 400 Invalid request, missing eco name
 */
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


// Start NodeJS Server
app.listen(port, () => {
    console.log("Starting up Server-Essentials Rest API using port '" + port + "'");
});
