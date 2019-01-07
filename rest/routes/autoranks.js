const express = require("express");
const router = require("express-promise-router")();

const AutoRankController = require("../controllers/autoranks");

router.route("/add").post(AutoRankController.add);
router.route("/find/:name").get(AutoRankController.find);
router.route("/find").get(AutoRankController.findAll);
router.route("").get(AutoRankController.findAll);
router.route("/find/:name/delete").delete(AutoRankController.delete);
router.route("/find/:name/override").put(AutoRankController.override);

module.exports = router;