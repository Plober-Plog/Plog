import React from "react";
import Btn from "./Btn";
<<<<<<< HEAD

const ModalConfirm = ({open, onClose, onConfirm, title, content, confirmText}) => {
=======
import './ModalComfirm.css'; // CSS 파일을 임포트

const ModalConfirm = ({ open, onClose, onConfirm, title, content, confirmText }) => {
>>>>>>> master
  if (!open) {
    return null;
  }

  return (
<<<<<<< HEAD
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
=======
    <div className="modal-overlay">
      <div className="modal-content">
        <h1 className="modal-title">{title}</h1>
        <p className="modal-text">{content}</p>
        <div className="modal-buttons">
        <button
            onClick={onClose}
            className="modal-cancel-btn"
          >
            취소
          </button>
          <button
            onClick={onConfirm}
            className="modal-confirm-btn"
          >
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
}

export default ModalConfirm;
>>>>>>> master
