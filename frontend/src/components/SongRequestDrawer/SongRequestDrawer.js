import React from "react";
import { Drawer, Button, Form, Input } from "antd";

function SongRequestDrawer(props) {
  return (
    <Drawer
      title="Pedir música"
      placement="right"
      onClose={props.onDrawerClose}
      visible={props.isVisible}
      width={window.innerWidth > 900 ? 500 : window.innerWidth - 100}
    >
      <Form
        form={props.form}
        onFinish={props.onFinish}
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
      >
        <Form.Item
          label="Nome da música"
          name="songName"
          rules={[{ required: true, message: "Informe o nome de uma música!" }]}
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
  );
}

export default SongRequestDrawer;
