const express = require("express");
const router = require('express-promise-router')();

const UsersController = require("../controllers/users");

router.route("/add").post(UsersController.add);
router.route("/find/:uuid").get(UsersController.find);
router.route("/find").get(UsersController.findAll);
router.route("").get(UsersController.findAll);
router.route("/find/:uuid/delete").delete(UsersController.delete);
router.route("/find/:uuid/override").put(UsersController.override);

module.exports = router;