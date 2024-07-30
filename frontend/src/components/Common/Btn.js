import React from 'react';
import PropTypes from 'prop-types';

const Btn = ({ content, onClick }) => {
    return (
      <button
        onClick={onClick}
      >
        {children}
      </button>
    );
  };

  