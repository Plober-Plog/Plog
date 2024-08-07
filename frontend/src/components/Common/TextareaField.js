import React from 'react';

const TextareaField = ({ type, placeholder, value, onChange, isrequired, className, disabled }) => {
  return (
    <div>
      <textarea 
        type={type} 
        placeholder={placeholder} 
        value={value} 
        onChange={onChange} 
        required={isrequired}
        className={className}
        disabled={disabled}
      />
    </div>
  );
}

export default TextareaField;
