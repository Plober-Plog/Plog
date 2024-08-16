import React from 'react';
import PropTypes from 'prop-types';
<<<<<<< HEAD

const Btn = ({ content, onClick, style, disabled, className }) => {
  return (
    <button onClick={onClick} style={style} disabled={disabled} className={className}>
=======
import './Btn.css'

const Btn = ({ content, onClick, style, disabled, className }) => {
  return (
    <button
      className='btn'
      onClick={onClick} style={style} disabled={disabled}>
>>>>>>> master
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