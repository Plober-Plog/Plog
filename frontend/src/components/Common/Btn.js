import React from 'react';
import PropTypes from 'prop-types';

const Btn = ({ content, onClick, style, disabled, className }) => {
  return (
    <button onClick={onClick} style={style} disabled={disabled} className={className}>
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