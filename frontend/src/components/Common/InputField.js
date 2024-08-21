import React from 'react';

const InputField = ({ type, placeholder, value, onChange, isrequired, className, disabled }) => {
  const today = new Date().toISOString().split("T")[0];

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
