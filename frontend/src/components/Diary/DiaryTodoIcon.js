import React from 'react';
import PropTypes from 'prop-types';

const DiaryTodoIcon = ({ src, active, onClick }) => (
    <img
        src={src}
        alt="todo icon"
        style={{ width: '50px', height: '50px', opacity: active ? 1 : 0.5 , cursor: 'pointer'}}
        onClick={onClick}
    />
);

DiaryTodoIcon.propTypes = {
    src: PropTypes.string.isRequired,
    active: PropTypes.bool.isRequired,
    onClick: PropTypes.func.isRequired,
};

export default DiaryTodoIcon;