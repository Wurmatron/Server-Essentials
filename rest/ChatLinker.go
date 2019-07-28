package main

import (
	"github.com/gorilla/websocket"
	"log"
	"net/http"
)

var socket = websocket.Upgrader{}

func Repeat(w http.ResponseWriter, r *http.Request) {
	c, err := socket.Upgrade(w, r, nil)
	if err != nil {
		log.Print("upgrade:", err)
		return
	}
	defer c.Close()
	for {
		mt, message, err := c.ReadMessage()
		if err != nil {
			log.Println("Read:", err)
			break
		}
		log.Printf("Revived: %s", message)
		err = c.WriteMessage(mt, message)
		if err != nil {
			log.Println("write:", err)
			break
		}
	}
}
