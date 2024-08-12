import React from "react";
import Btn from "./Btn";
import './ModalComplete.css'; // CSS 파일을 임포트

const ModalComplete = ({ open, onClose, title, content }) => {
  if (!open) {
    return null;
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h1 className="modal-title">{title}</h1>
        <p className="modal-text">{content}</p>
        <Btn 
          content='확인'
          onClick={onClose}
          className="modal-btn"
        />
      </div>
    </div>
  );
}

export default ModalComplete;
