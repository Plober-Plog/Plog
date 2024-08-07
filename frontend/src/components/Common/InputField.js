import React from 'react';

const InputField = ({ type, placeholder, value, onChange, isrequired, className, disabled }) => {
  return (
    <div>
      <input 
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

export default InputField;
