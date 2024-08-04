import React from 'react';

const InputField = ({ type, placeholder, value, onChange, isrequired, className }) => {
  return (
    <div>
      <input 
        type={type} 
        placeholder={placeholder} 
        value={value} 
        onChange={onChange} 
        required={isrequired}
        className={className}
      />
    </div>
  );
}

export default InputField;
