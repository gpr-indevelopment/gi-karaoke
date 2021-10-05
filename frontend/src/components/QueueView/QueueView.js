import React, { useState } from "react";
import { Avatar, Button, Spin, Form, Typography } from "antd";
import styles from "./QueueView.module.css";
import SongPurple from "../../assets/song-purple.png";
import SongRequestQueue from "../SongRequestQueue/SongRequestQueue";
import NowPlaying from "../NowPlaying/NowPlaying";
import SongRequestDrawer from "../SongRequestDrawer/SongRequestDrawer";
import { config } from "../../constants";

const { Title } = Typography;

function QueueView() {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [drawerVisible, setDrawerVisible] = useState(false);

  function onDrawerClose() {
    setDrawerVisible(false);
  }

  function onDrawerFinish(values) {
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
      form.resetFields();
      setDrawerVisible(false);
      setLoading(false);
    });
  }

  return (
    <Spin spinning={loading}>
      <div className={styles.container}>
        <SongRequestDrawer
          isVisible={drawerVisible}
          onClose={onDrawerClose}
          onFinish={onDrawerFinish}
          form={form}
        />
        <div className={styles.innerContainer}>
          <Title style={{ color: "violet", textAlign: "center" }}>
            Karaokê da Gi 19
          </Title>
          <div className={styles.nowPlayingContainer}>
            <Avatar
              src={SongPurple}
              style={{ marginBottom: "15px", marginRight: "15px" }}
            />
            <Title level={2} style={{ color: "white" }}>
              Tocando agora
            </Title>
            <Avatar
              src={SongPurple}
              style={{ marginBottom: "15px", marginLeft: "15px" }}
            />
          </div>
          <NowPlaying />
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
          <SongRequestQueue />
        </div>
      </div>
    </Spin>
  );
}

export default QueueView;
