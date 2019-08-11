package main

import (
	"bytes"
	"encoding/json"
	"flag"
	"fmt"
	"github.com/bwmarrin/discordgo"
	"math/rand"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"strings"
	"syscall"
)

var (
	Token     string
	Url       string
	RestToken string
)

const staffNotifyRank = "396007775655952384"

func init() {
	flag.StringVar(&Token, "t", "", "Bot Token")
	flag.StringVar(&Url, "w", "", "Rest URL")
	flag.StringVar(&RestToken, "r", "", "Rest Token")
	flag.Parse()
}

func main() {
	discord, err := discordgo.New("Bot " + Token)
	if err != nil {
		fmt.Println("error creating Discord session,", err)
		return
	}
	discord.AddHandler(messageCreate)

	err = discord.Open()
	if err != nil {
		fmt.Println("error opening connection,", err)
		return
	}

	fmt.Println("SE Discord Bot is now running.  Press CTRL-C to exit.")
	sc := make(chan os.Signal, 1)
	signal.Notify(sc, syscall.SIGINT, syscall.SIGTERM, os.Interrupt, os.Kill)
	<-sc
	discord.Close()

}

func messageCreate(s *discordgo.Session, m *discordgo.MessageCreate) {
	if m.Author.ID == s.State.User.ID {
		return
	}
	dm, _ := ComesFromDM(s, m)
	if dm {
		if (strings.HasPrefix(m.Content, "!verify")) {
			authKey := rand.Int()
			s.ChannelMessageSend(m.ChannelID, "Enter the following code in-game: "+strconv.Itoa(authKey))
			s.ChannelMessageSend(m.ChannelID, "You have 10 minutes before this expires or you will need another one.")
			data := DiscordToken{
				DiscordID: m.Author.ID,
				Token:     strconv.Itoa(authKey),
			}
			d, _ := json.MarshalIndent(data, "", " ")
			fmt.Print("DATA: " + string(d))
			setDiscord(d)
		}
	}
	//	s.ChannelMessageSend(msg.ChannelID, "<@&" + staffNotifyRank + ">")
}

// https://github.com/bwmarrin/discordgo/wiki/FAQ#checking-if-a-message-is-from-a-private-channel
func ComesFromDM(s *discordgo.Session, m *discordgo.MessageCreate) (bool, error) {
	channel, err := s.State.Channel(m.ChannelID)
	if err != nil {
		if channel, err = s.Channel(m.ChannelID); err != nil {
			return false, err
		}
	}
	return channel.Type == discordgo.ChannelTypeDM, nil
}

func setDiscord(data []byte) {
	tr := &http.Transport{
		MaxIdleConnsPerHost: 10,
	}
	client := http.Client{
		Transport: tr,
	}
	req, err := http.NewRequest("POST", Url+"discord/add", bytes.NewBuffer(data))
	if err == nil {
		req.Header.Add("Authorization", "Basic "+RestToken)
		resp, _ := client.Do(req)
		resp.Body.Close()
		//fmt.Println("Token created for '" + data.Token + "'")
	} else {
		fmt.Print(err.Error())
	}
}

//func getUUIDFromUserName(name string) string {
//	response, err := http.Get(strings.Join([]string{"https://mcapi.de/api/user/", name}, ""))
//	if err != nil {
//		fmt.Println(err)
//		return "The HTTP request failed"
//	} else {
//		f, _ := ioutil.ReadAll(response.Body)
//		data := make(map[string]interface{})
//		err := json.Unmarshal(f, &data)
//		if err != nil {
//			fmt.Println(err)
//			return "The HTTP request failed"
//		}
//		userUUID := data["uuid"].(string)
//		return uuid.MustParse(userUUID).String()
//	}
//}
//
//func IsValidUUID(u string) bool {
//	_, err := uuid.Parse(u)
//	return err == nil
//}
