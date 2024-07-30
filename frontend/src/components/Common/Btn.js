import React from 'react';
import PropTypes from 'prop-types';

const Btn = ({ content, onClick, style, disabled }) => {
  return (
    <button onClick={onClick} style={style} disabled={disabled}>
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