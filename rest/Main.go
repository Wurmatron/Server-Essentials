package main

import (
	b64 "encoding/base64"
	"fmt"
	"github.com/go-redis/redis"
	mux "github.com/julienschmidt/httprouter"
	"log"
	"net/http"
	"os"
)

// Rest Config
const version string = "0.0.6"
const address string = ":5050"
const httpsCert string = "fullchain.pem"
const httpsKey string = "privkey.pem"
const defaultUser string = "admin"

// Redis Config
const redisAddress string = "localhost:6379"
const redisPass string = ""
const redisDatabase int = 0
const redisDatabaseUser = redisDatabase
const redisDatabaseRank = redisDatabaseUser + 1
const redisDatabaseAutoRank = redisDatabaseRank + 1
const redisDatabaseTeam = redisDatabaseAutoRank + 1
const redisDatabaseEco = redisDatabaseTeam + 1
const redisDatabaseStatus = redisDatabaseEco + 1
const redisDatabaseTransfer = redisDatabaseStatus + 1
const redisDatabaseDiscord = redisDatabaseTransfer + 1
const redisDatabaseLookup = redisDatabaseDiscord + 1
const redisDatabaseBan = redisDatabaseLookup + 1
const redisDatabaseAuth = redisDatabaseBan + 1

var redisDBAuth *redis.Client

func main() {
	fmt.Println("Loading Rest-API v" + version + " on " + address)
	router := NewRouter()
	_, err := newClient(0).Ping().Result()
	if err != nil {
		panic("Unable to connect to RedisDB ")
	}
	fmt.Println("Connected to redis at " + redisAddress + " starting on DataBase " + string(redisDatabase))
	redisDBAuth = newClient(redisDatabaseAuth)
	SetupDefaultAuth()
	log.Fatal(http.ListenAndServeTLS(address, httpsCert, httpsKey, router))
}

func NewRouter() *mux.Router {
	router := mux.New()
	for _, route := range routes {
		if route.RequireAuth {
			router.Handle(route.Method, route.Pattern, auth(route.Handle))
		} else {
			router.Handle(route.Method, route.Pattern, route.Handle)
		}
	}
	return router
}

func newClient(databaseIndex int) *redis.Client {
	return redis.NewClient(&redis.Options{
		Addr:     redisAddress,
		Password: redisPass,
		DB:       redisDatabase + databaseIndex,
	})
}

func SetupDefaultAuth() {
	if redisDBAuth.Exists(defaultUser).Val() == 0 || len(os.Args) > 1 && os.Args[2] == "--resetAuth" {
		pass := CreateDefaultPassword()
		redisDBAuth.Set(defaultUser, b64.StdEncoding.EncodeToString([]byte(pass)), 0)
		fmt.Println("The default login info is: " + defaultUser + ":" + pass)
		fmt.Println("Make sure to save and place the login in a safe place. start this up with --resetAuth to reset the auth login")
	}
}
