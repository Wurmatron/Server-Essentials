const express = require("express");
const router = require("express-promise-router")();

const RankController = require("../controllers/ranks");

router.route("/add").post(RankController.add);
router.route("/find/:name").get(RankController.find);
router.route("/find").get(RankController.findAll);
router.route("").get(RankController.findAll);
router.route("/find/:name/delete").delete(RankController.delete);
router.route("/find/:name/override").put(RankController.override);

module.exports = router;