const express = require("express");
const router = require('express-promise-router')();

const EcoController = require("../controllers/eco");

router.route("/add").post(EcoController.add);
router.route("/find/:name").get(EcoController.find);
router.route("/find").get(EcoController.findAll);
router.route("").get(EcoController.findAll);
router.route("/find/:name/delete").delete(EcoController.delete);
router.route("/find/:name/override").put(EcoController.override);

module.exports = router;