import React from 'react';
import PropTypes from 'prop-types';

const DiaryTodoIcon = ({ src }) => (
    <img src={src} alt="todo icon" style={{ width: '50px', height: '50px' }} />
);

DiaryTodoIcon.propTypes = {
    src: PropTypes.string.isRequired,
};

export default DiaryTodoIcon;