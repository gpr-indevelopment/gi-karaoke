import React, { useState, useEffect } from "react";
import { List, Avatar, Spin } from "antd";
import styles from "./NowPlaying.module.css";
import { config } from "../../constants";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function NowPlaying() {
  const [loading, setLoading] = useState(false);
  const [currentlyPlaying, setCurrentlyPlaying] = useState([]);

  useEffect(() => {
    var socket = new SockJS(config.url + "/update");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      stompClient.subscribe("/topic/update", function (messageOutput) {
        updateCurrentlyPlaying();
      });
    });
    updateCurrentlyPlaying();
  }, []);

  function resolveDateString(date) {
    let timeString = new Date(date).toLocaleTimeString();
    return timeString;
  }

  function updateCurrentlyPlaying() {
    fetch(config.url + "/karaoke/currently-playing")
      .then((res) => {
        if (res.ok) {
          return res.json();
        } else {
          setLoading(false);
          setCurrentlyPlaying([]);
        }
      })
      .then((body) => {
        if (body) {
          setCurrentlyPlaying([body]);
        }
        setLoading(false);
      });
  }

  return (
    <Spin spinning={loading}>
      <div className={styles.listContainer}>
        <List
          itemLayout="horizontal"
          dataSource={currentlyPlaying}
          bordered
          locale={{ emptyText: "Nenhuma música =/" }}
          renderItem={(item) => (
            <List.Item>
              <List.Item.Meta
                avatar={
                  <Avatar src="https://cdn0.iconfinder.com/data/icons/audio-vol-1b/100/1-41-512.png" />
                }
                title={item.songName}
                description={item.requester}
              />
              <div>{resolveDateString(item.requestedAt)}</div>
            </List.Item>
          )}
        />
      </div>
    </Spin>
  );
}

export default NowPlaying;
