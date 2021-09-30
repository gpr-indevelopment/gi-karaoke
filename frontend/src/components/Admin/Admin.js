import React, { useState } from "react";
import styles from "./Admin.module.css";
import { Button, Typography, Spin } from "antd";
import { config } from "../../constants";

const { Title } = Typography;

function Admin() {
  const [loading, setLoading] = useState(false);

  function nextSong() {
    setLoading(true);
    fetch(config.url + "/karaoke/next-song", {
      method: "POST",
    }).then(() => setLoading(false));
  }

  function revert() {
    setLoading(true);
    fetch(config.url + "/karaoke/revert", {
      method: "POST",
    }).then(() => setLoading(false));
  }

  return (
    <Spin spinning={loading}>
      <div className={styles.container}>
        <div className={styles.innerContainer}>
          <Title>Admin</Title>
          <div className={styles.buttonContainer}>
            <Button
              type="primary"
              size="large"
              style={{ width: "150px" }}
              onClick={nextSong}
            >
              Próxima música
            </Button>
          </div>
          <div className={styles.buttonContainer}>
            <Button
              type="primary"
              size="large"
              style={{ width: "150px" }}
              onClick={revert}
            >
              Reverter
            </Button>
          </div>
        </div>
      </div>
    </Spin>
  );
}

export default Admin;
