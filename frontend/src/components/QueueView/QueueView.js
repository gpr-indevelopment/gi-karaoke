import { config } from "../../constants";
import React, { useState, useEffect } from "react";
import {
  List,
  Avatar,
  Drawer,
  Button,
  Form,
  Input,
  Spin,
  Typography,
} from "antd";
import styles from "./QueueView.module.css";
import SongPurple from "../../assets/song-purple.png"
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const { Title } = Typography;

function QueueView() {
  useEffect(() => {
    var socket = new SockJS(config.url + "/update");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
      stompClient.subscribe('/topic/update', function(messageOutput) {
        updateSongList();
    });
    });
    updateSongList();
  }, []);

  const [songQueue, setSongQueue] = useState([]);
  const [currentlyPlaying, setCurrentlyPlaying] = useState([]);
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  function updateSongList() {
    setLoading(true);
    fetch(config.url + "/karaoke")
      .then((res) => res.json())
      .then((body) => {
        setSongQueue(body);
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
              console.log(body);
              setCurrentlyPlaying([body]);
            }
            setLoading(false);
          });
      });
  }

  function resolveDateString(date) {
    let timeString = new Date(date).toLocaleTimeString();
    return timeString;
  }

  function onDrawerClose() {
    setDrawerVisible(false);
  }

  function onFinish(values) {
    setLoading(true);
    var tzoffset = new Date().getTimezoneOffset() * 60000;
    var localISOTime = new Date(Date.now() - tzoffset)
      .toISOString()
      .slice(0, -1);
    values.requestedAt = localISOTime;
    fetch(config.url + "/karaoke", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    }).then(() => {
      updateSongList();
      form.resetFields();
      setDrawerVisible(false);
    });
  }

  return (
    <Spin spinning={loading}>
      <div className={styles.container}>
        <Drawer
          title="Pedir música"
          placement="right"
          onClose={onDrawerClose}
          visible={drawerVisible}
          width={window.innerWidth > 900 ? 500 : window.innerWidth - 100}
        >
          <Form
            form={form}
            onFinish={onFinish}
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
          >
            <Form.Item
              label="Nome da música"
              name="songName"
              rules={[
                { required: true, message: "Informe o nome de uma música!" },
              ]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="Seu nome"
              name="requester"
              rules={[{ required: true, message: "Informe seu nome!" }]}
            >
              <Input />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
              <Button type="primary" htmlType="submit">
                Enviar
              </Button>
            </Form.Item>
          </Form>
        </Drawer>
        <div className={styles.innerContainer}>
            <Title style={{ color: "violet", textAlign:"center" }}>
              Karaokê da Gi 19
            </Title>
          <div className={styles.nowPlayingContainer}>
            <Avatar src={SongPurple} style={{marginBottom:"15px", marginRight:"15px"}}/>
            <Title level={2} style={{ color: "white" }}>
              Tocando agora
            </Title>
            <Avatar src={SongPurple} style={{marginBottom:"15px" , marginLeft:"15px"}}/>
          </div>
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
          <div className={styles.buttonHeader}>
            <Button
              type="primary"
              onClick={() => setDrawerVisible(true)}
              style={{ width: "100%" }}
              size="large"
            >
              Pedir música
            </Button>
          </div>
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
        </div>
      </div>
    </Spin>
  );
}

export default QueueView;
