import {PageContainer} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {message, Table, TableProps} from "antd";
import {
  listInterfaceInfoVoPageUsingPost
} from "@/services/qapi-backend/interfaceInfoController";
import {useNavigate} from "@umijs/max";

/**
 * 主页
 * @constructor
 */
const Index: React.FC = () => {
  const title = 'QAPI 在线接口开发平台';
  const [data, setData] = useState<API.InterfaceInfoVO[]>([]);
  const [total, setTotal] = useState<number>(0);
  const navigate = useNavigate();
  const columns: TableProps<API.InterfaceInfoVO>['columns'] = [
    {
      title: '接口名称',
      dataIndex: 'name',
      key: 'name',
      render: (_, record) => (
        <a onClick={() => {
          navigate(`/interfaceInfo/${record.id}`, {
            replace: true
          })
        }}>{record.name}</a>
      ),
    },
    {
      title: '接口描述',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: '接口方法',
      dataIndex: 'method',
      key: 'method',
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      key: 'requestHeader',
      hidden: true,
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      key: 'responseHeader',
      hidden: true,
    },
  ];
  const loadData = async (current = 1, pageSize = 10) => {
    try {
      const res = await listInterfaceInfoVoPageUsingPost({
        current,
        pageSize
      });
      setData(res.data?.records ?? [])
      setTotal(res.data?.total ?? 0)
      return true;
    } catch (error: any) {
      message.error(error.message);
      return false;
    }
  }
  useEffect(() => {
    loadData()
  }, [])

  return (
    <PageContainer title={title}>
      <Table
        rowKey={record => record.id as any}
        columns={columns}
        dataSource={data}
        pagination={{
          pageSize: 10,
          total,
          showTotal(total) {
            return `共 ${total} 条数据`
          },
          onChange(page, pageSize) {
            loadData(page, pageSize)
          }
        }}
      >
      </Table>
    </PageContainer>
  );
};

export default Index;
