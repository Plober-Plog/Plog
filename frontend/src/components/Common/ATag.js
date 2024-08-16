<<<<<<< HEAD
const ATag = ({content, onClick}) => {
  return (
    <a onClick={onClick}>
=======
const ATag = ({content, onClick, className}) => {
  return (
    <a onClick={onClick} className={className}>
>>>>>>> master
      {content}
    </a>
  )
}

export default ATag;