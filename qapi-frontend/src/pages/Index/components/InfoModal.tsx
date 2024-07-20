import {
  type ProColumns, ProFormInstance, ProTable
} from '@ant-design/pro-components';
import '@umijs/max';
import React, {useEffect, useRef} from 'react';
import {Button, Modal, TableProps} from "antd";

export type Props = {
  values: API.InterfaceInfoVO;
  onCancel: () => void;
  open: boolean;
};
const InfoModal: React.FC<Props> = (props) => {
  const {values, open, onCancel} = props;
  const formRef = useRef<ProFormInstance>()
  const columns: TableProps<API.InterfaceInfoVO>['columns'] = [
    {
      title: '接口名称',
      dataIndex: 'name',
      key: 'name',
      render: (text) => <a>{text}</a>,
    },
    {
      title: '接口方法',
      dataIndex: 'method',
      key: 'method',
    },
    {
      title: '接口描述',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      key: 'requestHeader',
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      key: 'responseHeader',
    },
  ];
  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(values);
    }
  }, [values])
  return (
    <Modal open={open} onCancel={() => onCancel?.()} footer={null}>
      <ProTable
        type={'form'}
        columns={columns as any}
        formRef={formRef}
      />
    </Modal>
  )
};
export default InfoModal;
