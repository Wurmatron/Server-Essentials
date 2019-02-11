package main

import (
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis"
	mux "github.com/julienschmidt/httprouter"
	"io/ioutil"
	"net/http"
)

var redisDBTeam *redis.Client
var redisDBUser *redis.Client

func init() {
	redisDBTeam = newClient(redisDatabaseTeam)
	redisDBUser = newClient(redisDatabaseUser)
}

func GetTeam(w http.ResponseWriter, _ *http.Request, p mux.Params) {
	name := string(p[0].Value)
	if redisDBTeam.Exists(name).Val() == 1 {
		w.Header().Set("content-type", "application/json")
		w.Header().Set("version", version)
		w.Write([]byte(redisDBTeam.Get(name).Val()))
	} else {
		w.WriteHeader(http.StatusNotFound)
	}
}

func SetTeam(w http.ResponseWriter, r *http.Request, _ mux.Params) {
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	var team Team
	err = json.Unmarshal(b, &team)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	output, err := json.Marshal(team)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	redisDBTeam.Set(team.Name, output, 0)
	updatePlayer(team.Owner, team.Name)
	for entry := range team.Members {
		updatePlayer(team.Members[entry], team.Name)
	}
	w.WriteHeader(http.StatusCreated)
}

func GetAllTeams(w http.ResponseWriter, _ *http.Request, _ mux.Params) {
	var data []Team
	for entry := range redisDBTeam.Keys("*").Val() {
		var team Team
		json.Unmarshal([]byte(redisDBTeam.Get(redisDBTeam.Keys("*").Val()[entry]).Val()), &team)
		data = append(data, team)
	}
	output, err := json.MarshalIndent(data, " ", " ")
	if err != nil {
		fmt.Fprintln(w, "{}")
		return
	}
	fmt.Fprintln(w, string(output))
}

func updatePlayer(uuid, team string) {
	if len(uuid) > 0 {
		var globalUser GlobalUser
		json.Unmarshal([]byte(redisDBuser.Get(uuid).Val()), &globalUser)
		globalUser.Team = team
		output, _ := json.MarshalIndent(globalUser, " ", " ")
		redisDBUser.Set(globalUser.UUID, output, 0)
	}
}
