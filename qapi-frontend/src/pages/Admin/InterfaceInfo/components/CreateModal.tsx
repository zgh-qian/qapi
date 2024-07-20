import {
  type ProColumns, ProTable
} from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';
import {Modal} from "antd";

export type Props = {
  columns: ProColumns<API.InterfaceInfo>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  open: boolean;
};
const CreateModal: React.FC<Props> = (props) => {
  const {columns, open, onCancel, onSubmit} = props;
  return (
    <Modal open={open} onCancel={() => onCancel?.()} footer={null}>
      <ProTable
        type={'form'}
        columns={columns}
        onSubmit={async (value) => {
          onSubmit?.(value)
        }}
      />
    </Modal>
  )
};
export default CreateModal;
