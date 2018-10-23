const express = require("express");
const morgan = require("morgan");
const bodyParser = require("body-parser");

const app = express();

// Middleware
app.use(morgan("dev"));
app.use(bodyParser.json());

// Routes
app.use("/users",require("./routes/users"));
app.use("/ranks",require("./routes/ranks"));

// Start Server
const port = 8080;
app.listen(port);
console.log("Server listening on port " + port);