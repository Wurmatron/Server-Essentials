const express = require("express");
const router = require('express-promise-router')();

const TeamController = require("../controllers/teams");

router.route("/add").post(TeamController.add);
router.route("/find/:name").get(TeamController.find);
router.route("/find").get(TeamController.findAll);
router.route("").get(TeamController.findAll);
router.route("/find/:name/delete").delete(TeamController.delete);
router.route("/find/:name/override").put(TeamController.override);

module.exports = router;