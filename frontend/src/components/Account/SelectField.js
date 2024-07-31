import React from 'react';

const SelectField = ({ value, onChange, options, isRequired }) => {
  return (
    <div>
      <select
        value={value}
        onChange={onChange}
        required={isRequired}
      >
        {options.map(option => (
          <option key={option} value={option}>{option}</option>
        ))}
      </select>
    </div>
  );
}

export default SelectField;