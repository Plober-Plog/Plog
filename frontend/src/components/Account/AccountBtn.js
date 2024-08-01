import './Login.css'

const AccountBtn = ({content, onClick}) => {
  return (
    <a onClick={onClick}>
      {content}
    </a>
  )
}

export default AccountBtn;