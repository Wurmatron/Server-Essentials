package main

import (
	"fmt"
	"github.com/go-redis/redis"
	mux "github.com/julienschmidt/httprouter"
	"log"
	"net/http"
)

// Rest Config
const version string = "0.0.2"
const address string = ":5050"
const httpsCert string = "fullchain.pem"
const httpsKey string = "privkey.pem"

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

func main() {
	fmt.Println("Loading Rest-API v" + version + " on " + address)
	router := NewRouter()
	log.Fatal(http.ListenAndServeTLS(address, httpsCert, httpsKey, router))
	_, err := newClient(0).Ping().Result()
	if err != nil {
		panic("Unable to connect to RedisDB ")
	}
	fmt.Println("Connected to redis at " + redisAddress + " starting on DataBase " + string(redisDatabase))
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
