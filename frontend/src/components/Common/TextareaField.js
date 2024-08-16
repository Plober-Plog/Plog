import React from 'react';
<<<<<<< HEAD
=======
import './TextareaField.css';
>>>>>>> master

const TextareaField = ({ type, placeholder, value, onChange, isrequired, className, disabled }) => {
  return (
    <div>
      <textarea 
        type={type} 
        placeholder={placeholder} 
        value={value} 
        onChange={onChange} 
        required={isrequired}
<<<<<<< HEAD
        className={className}
=======
        className={`textarea-field ${className}`}
>>>>>>> master
        disabled={disabled}
      />
    </div>
  );
}

export default TextareaField;
