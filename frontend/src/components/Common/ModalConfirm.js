import React from "react";
import Btn from "./Btn";

const ModalConfirm = ({open, onClose, onConfirm, title, content, confirmText}) => {
  if (!open) {
    return null;
  }

  return (
    <div>
      <h1>{title}</h1>
      <p>{content}</p>
      <div>
        <Btn 
          content='취소'
          onClick={onClose}
        />
        <Btn 
          content={confirmText}
          onClick={onConfirm}
        />
      </div>
    </div>
  )
}

export default ModalConfirm;