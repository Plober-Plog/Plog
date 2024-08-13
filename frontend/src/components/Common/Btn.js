import React from 'react';
import PropTypes from 'prop-types';
import './Btn.css'

const Btn = ({ content, onClick, style, disabled, className }) => {
  return (
    <button
      className='btn'
      onClick={onClick} style={style} disabled={disabled}>
      {content}
    </button>
  );
};

Btn.propTypes = {
  content: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
  style: PropTypes.object,
  disabled: PropTypes.bool,
};


export default Btn;