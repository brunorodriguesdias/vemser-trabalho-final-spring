<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f2f2f2;
            }
            .container {
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                padding: 20px;
                border: 1px solid #cccccc;
                border-radius: 5px;
            }
            h1 {
                font-size: 28px;
                color: #333333;
                margin-top: 0;
            }
            p {
                font-size: 18px;
                color: #666666;
                margin-bottom: 20px;
            }
            h2 {
                font-size: 24px;
                color: #333333;
                margin-top: 40px;
            }
            .container img {
                display: block;
                margin: 0 auto;
            }
        </style>
    </head>

    <body>
    <div class="container">
        <img src="https://raw.githubusercontent.com/gabrielSdejesus/vemser-trabalho-final-spring/main/images/logo.png" width="200" alt="Javamos Decolar">
        <h1>Olá, ${nome}</h1>
        <p>Seja bem-vindo!</p>
        <p>Viemos informar que a sua compra código ${codigo} foi cancelada.<p>
        <p>Qualquer dúvida, por favor, nos contate no email <a href="mailto:${email}">${email}</a>.</p>
        <p>Atenciosamente,</p>
        <h2>Equipe Javamos Decolar!</h2>
    </div>
    </body>
</html>


