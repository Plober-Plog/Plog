import React from "react";
import Btn from "./Btn";
<<<<<<< HEAD

const ModalComplete = ({open, onClose, title, content}) => {
=======
import './ModalComplete.css'; // CSS 파일을 임포트

const ModalComplete = ({ open, onClose, title, content }) => {
>>>>>>> master
  if (!open) {
    return null;
  }

  return (
<<<<<<< HEAD
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
=======
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
>>>>>>> master
