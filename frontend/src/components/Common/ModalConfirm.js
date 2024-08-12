import React from "react";
import Btn from "./Btn";
import './ModalComfirm.css'; // CSS 파일을 임포트

const ModalConfirm = ({ open, onClose, onConfirm, title, content, confirmText }) => {
  if (!open) {
    return null;
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h1 className="modal-title">{title}</h1>
        <p className="modal-text">{content}</p>
        <div className="modal-buttons">
          <Btn 
            content='취소'
            onClick={onClose}
            className="modal-cancel-btn"
          />
          <Btn 
            content={confirmText}
            onClick={onConfirm}
            className="modal-confirm-btn"
          />
        </div>
      </div>
    </div>
  );
}

export default ModalConfirm;
