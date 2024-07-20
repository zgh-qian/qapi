import {PageContainer} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {Button, Card, Descriptions, Form, message} from "antd";
import {
  getInterfaceInfoUsingGet, invokeInterfaceInfoUsingPost,
} from "@/services/qapi-backend/interfaceInfoController";
import {useParams} from "react-router";
import TextArea from "antd/es/input/TextArea";
import {FormProps} from "antd/lib";

/**
 * 接口详情
 * @constructor
 */
const InterfaceInfo: React.FC = () => {
  const title = 'QAPI 接口详情';
  const params = useParams()
  const [invokeLoading, setInvokeLoading] = useState<boolean>(false)
  const [invokeRes, setInvokeRes] = useState<any>()
  const [data, setData] = useState<API.InterfaceInfoVO>({});
  const loadData = async () => {
    if (!params.id) {
      message.error("id 不存在");
      return;
    }
    try {
      const res = await getInterfaceInfoUsingGet({id: params?.id as any});
      setData(res.data ?? {})
    } catch (error: any) {
      message.error(error.message);
    }
  }
  useEffect(() => {
    loadData();
  }, [])
  const onFinish: FormProps["onFinish"] = async (values) => {
    setInvokeLoading(true);
    if (!params.id) {
      message.error("id 不存在");
      return;
    }
    try {
      const res = await invokeInterfaceInfoUsingPost({
        id: params.id,
        ...values
      })
      console.log(res);
      setInvokeRes(res.data);
    } catch (error: any) {
      message.error(error.message);
    }
    setInvokeLoading(false);
  };
  return (
    <PageContainer title={title}>
      <Card>
        {
          data && <Descriptions title={data.name} column={1} extra={<Button type={"primary"}>调用</Button>}>
            <Descriptions.Item label="请求方法">{data.method}</Descriptions.Item>
            <Descriptions.Item label="请求地址">{data.url}</Descriptions.Item>
            <Descriptions.Item label="请求参数">{data.requestParams}</Descriptions.Item>
            <Descriptions.Item label="请求头">{data.requestHeader}</Descriptions.Item>
            <Descriptions.Item label="响应头">{data.responseHeader}</Descriptions.Item>
            <Descriptions.Item label="描述">{data.description}</Descriptions.Item>
            <Descriptions.Item label="调用次数">{data.callCount}</Descriptions.Item>
            <Descriptions.Item label="接口状态">{data.status === 0 ? '关闭' : '开启'}</Descriptions.Item>
          </Descriptions>
        }
      </Card>
      <Card title={"调用接口"}>
        <Form
          name="invoke"
          onFinish={onFinish}
          layout={"vertical"}
        >
          <Form.Item
            label="请求参数"
            name="userRequestParams"
          >
            <TextArea/>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={invokeLoading}>
              调用
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title={"调用结果"}>
        {invokeRes}
      </Card>
    </PageContainer>
  );
};

export default InterfaceInfo;
