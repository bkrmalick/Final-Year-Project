import Popup from 'react-popup'

export function binaryPopup(titleText, contentText, leftText = 'NO', rightText = 'YES', leftCallback = closePopup, rightCallback = closePopup)
{
    return Popup.create({
        title: titleText,
        content: contentText,
        buttons: {
            left: [{
                text: leftText,
                className: 'danger',
                action: leftCallback

            }],
            right: [{
                text: rightText,
                className: 'success',
                action: rightCallback
            }]
        }
    });
}

function closePopup()
{
    Popup.close();
}