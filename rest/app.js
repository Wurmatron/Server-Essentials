const express = require("express");
const morgan = require("morgan");
const bodyParser = require("body-parser");

const app = express();

// Middleware
app.use(morgan("dev"));
app.use(bodyParser.json());

// Routes
app.use("/users", require("./routes/users"));
app.use("/ranks", require("./routes/ranks"));
app.use("/autoranks", require("./routes/autoranks"));
app.use("/teams", require("./routes/teams"));
app.use("/eco", require("./routes/eco"));

// Start Server
const port = 8080;
app.listen(port);
console.log("Server listening on port " + port);