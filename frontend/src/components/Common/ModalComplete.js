import React from "react";
import Btn from "./Btn";

const ModalComplete = ({open, onClose, title, content}) => {
  if (!open) {
    return null;
  }

  return (
    <div>
      <h1>{title}</h1>
      <p>{content}</p>
      <Btn 
        content='확인'
        onClick={onClose}
      />
    </div>
  )
}

export default ModalComplete;