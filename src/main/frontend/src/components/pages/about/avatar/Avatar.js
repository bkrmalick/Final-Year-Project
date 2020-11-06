import React, {useState } from 'react'

//styles
import './Avatar.scss'

function Avatar(props)
{
    const [cssClass, setCC] = useState("avatar-img");

    return <div className="avatar" >
        <div className={cssClass} >
            <img alt={props.imgAlt} src={props.imgSrc} onLoad={() =>setCC("avatar-img-ring")}  />
        </div><br />
        <h2>{props.title}</h2>
        <p>{props.name}</p>
    </div>;
}

export default Avatar