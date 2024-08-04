import React from "react";
import Btn from "../Common/Btn";
import './Modal.css'

const ModalComplete = ({ title, content, open, onClose }) => {
  if (!open) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h2>{title}</h2>
        <p>{content}</p>
        <Btn 
          content='확인'
          onClick={onClose}
        />
      </div>
    </div>
  );
};

export default ModalComplete;

