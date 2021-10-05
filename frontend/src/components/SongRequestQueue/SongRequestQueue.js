import React, { useState, useEffect } from "react";
import { List, Avatar, Spin } from "antd";
import styles from "./SongRequestQueue.module.css";
import { config } from "../../constants";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

function SongRequestQueue() {
  const [songQueue, setSongQueue] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    var socket = new SockJS(config.url + "/update");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      stompClient.subscribe("/topic/update", function (messageOutput) {
        updateSongList();
      });
    });
    updateSongList();
  }, []);

  function resolveDateString(date) {
    let timeString = new Date(date).toLocaleTimeString();
    return timeString;
  }

  function updateSongList() {
    setLoading(true);
    fetch(config.url + "/karaoke")
      .then((res) => res.json())
      .then((body) => {
        setSongQueue(body);
        setLoading(false);
      });
  }
  return (
    <Spin spinning={loading}>
      <div className={styles.listContainer}>
        <List
          itemLayout="horizontal"
          dataSource={songQueue}
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

export default SongRequestQueue;
