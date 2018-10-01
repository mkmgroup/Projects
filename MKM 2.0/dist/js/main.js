

$(document).ready(function () {
  "use strict";

  setTimeout(function(){
    $(".se-pre-con").fadeOut("slow");
  }, 2000);

  var db = firebase.firestore();

  db.collection("counter").doc("1")
    .onSnapshot(function(doc) {
      console.log("Current data: ", doc.data().count);
      document.getElementById('counter').innerText = doc.data().count.toString();
      document.getElementById('counterMobile').innerText = doc.data().count.toString();
    });



  document.getElementById("like").onclick = function(){



    var x = Number( document.getElementById('counter').innerText);

    document.getElementById('counter').innerText = (x + 1).toString();


    var sfDocRef = db.collection("counter").doc("1");

    return db.runTransaction(function(transaction) {
      // This code may get re-run multiple times if there are conflicts.
      return transaction.get(sfDocRef).then(function(sfDoc) {
        if (!sfDoc.exists) {
          throw "Document does not exist!";
        }

        var newPopulation = sfDoc.data().count + 1;
        transaction.update(sfDocRef, { count: newPopulation });
      });
    }).then(function() {
      console.log("Transaction successfully committed!");
    }).catch(function(error) {
      console.log("Transaction failed: ", error);
    });




  };
  document.getElementById("likeMobile").onclick = function(){



    var x = Number( document.getElementById('counterMobile').innerText);

    document.getElementById('counterMobile').innerText = (x + 1).toString();


    var sfDocRef = db.collection("counter").doc("1");

    return db.runTransaction(function(transaction) {
      // This code may get re-run multiple times if there are conflicts.
      return transaction.get(sfDocRef).then(function(sfDoc) {
        if (!sfDoc.exists) {
          throw "Document does not exist!";
        }

        var newPopulation = sfDoc.data().count + 1;
        transaction.update(sfDocRef, { count: newPopulation });
      });
    }).then(function() {
      console.log("Transaction successfully committed!");
    }).catch(function(error) {
      console.log("Transaction failed: ", error);
    });




  };




  setTimeout(function(){
    $('#integration').css({

      'visibility': 'visible',
      'opacity': '1',
      'color': '#F24F00',

    });
    }, 7000);
  setTimeout(function(){
    $('#titleMarketing').find('.titleBox').css({

      'color': '#F24F00',

    });
  }, 8000);
  setTimeout(function(){
    $('#titleTech').find('.titleBox').css({

      'color': '#F24F00',

    });
  }, 9000);
  setTimeout(function(){
    $('#titleEntertainment').find('.titleBox').css({

      'color': '#F24F00',

    });
  }, 10000);
  setTimeout(function(){
    $('.title').find('.titleBox').css({

      'color': '',

    });
    $('#integration').css({

      'visibility': '',
      'opacity': '',
      'color': '',

    });

  }, 16000);



  $('#menuButton').click(function() {
    $('.expandMenuMobile').css({

      'display': 'block',
      'height': '100vh',
      'width': '100vw',

    });

    $('.expandMenuMobile h1').css({
      'font-size': '40px',
      'display': 'inline-block',

    });
    $('#exitMenuButton').css({
      'visibility': 'visible',
      'opacity': '1',
      'display': 'block',

    });
    $('.expandMenuMobile img').css({
      'height': '30px',
      'width': 'auto',
      'display': 'inline-block',
    });

  });

  $('#exitMenuButton').click(function() {
    $('.expandMenuMobile').css({
      'height': '0',
      'width': '0',
    });

    $('.expandMenuMobile h1').css({
      'font-size': '0px',
      'display': 'none',

    });
    $('#exitMenuButton').css({
      'visibility': 'hidden',
      'opacity': '0',
      'display': 'none',
    });
    $('.expandMenuMobile img').css({
      'height': '0',
      'width': '0',
      'display': 'none',
    });
    setTimeout(function(){ $('.expandMenuMobile').css({
      'display': 'none',

    }); }, 2000);

  });

  var marketingOpen = false;
  var techOpen = false;
  var entertainment = false;


  if ($(window).width() <= 767) {

    $(document).mouseup(function(e)
    {
      var container = $("#titleMarketing");

      // if the target of the click isn't the container nor a descendant of the container
      if (!container.is(e.target) && container.has(e.target).length === 0)
      {
        container.find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        container.find('.foldingMenu li').css({
          'height': '0px'
        });
        container.find('.foldingMenu a').css({
          'font-size': '0px'
        });
        container.find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        container.find('.titleBox').css({
          'color' : 'white',
        });
        container.css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        marketingOpen = false;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '1%'
          });
          $('.bottom').css({
            'bottom': '0'
          });
          $('.homeWrapper').css({
            'height': '100%'
          });

        }
      }
      var container2 = $("#titleTech");

      // if the target of the click isn't the container nor a descendant of the container
      if (!container2.is(e.target) && container2.has(e.target).length === 0)
      {
        container2.find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        container2.find('.foldingMenu li').css({
          'height': '0px'
        });
        container2.find('.foldingMenu a').css({
          'font-size': '0px'
        });
        container2.find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        container2.find('.titleBox').css({
          'color' : 'white',
        });
        container2.css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        techOpen = false;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '1%'
          });
          $('.bottom').css({
            'bottom': '0'
          });
          $('.homeWrapper').css({
            'height': '100%'
          });

        }
      }

      var container3 = $("#titleEntertainment");

      // if the target of the click isn't the container nor a descendant of the container
      if (!container3.is(e.target) && container3.has(e.target).length === 0)
      {
        container3.find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        container3.find('.foldingMenu li').css({
          'height': '0px'
        });
        container3.find('.foldingMenu a').css({
          'font-size': '0px'
        });
        container3.find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        container3.find('.titleBox').css({
          'color' : 'white',
        });
        container3.css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        entertainment = false;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '1%'
          });
          $('.bottom').css({
            'bottom': '0'
          });
          $('.homeWrapper').css({
            'height': '100%'
          });

        }
      }
    });


    $('#titleMarketing').click(function() {


        if (marketingOpen) {
          $(this).find('.foldingMenu').css({
            'visibility': 'hidden',
            'opacity': '0'
          });
          $(this).find('.foldingMenu li').css({
            'height': '0px'
          });
          $(this).find('.foldingMenu a').css({
            'font-size': '0px'
          });
          $(this).find('.titleDescription').css({
            'visibility': 'hidden',
            'opacity': '0'
          });
          $(this).find('.titleBox').css({
            'color' : 'white',
          });
          $(this).css({
            'border': '1px solid #F24F00',
            'background': 'rgba(255, 255, 255, 0)',
            'height': '54px'
          });
          marketingOpen = false;
          if ($(window).height() <= 620) {

            $('.infoContainer').css({
              'bottom': '1%'
            });
            $('.bottom').css({
              'bottom': '0'
            });
            $('.homeWrapper').css({
              'height': '100%'
            });

          }
        }else {
          $(this).find('.foldingMenu').css({
            'visibility': 'visible',
            'opacity': '1'
          });
          $(this).find('.foldingMenu li').css({
            'height': '30px'
          });
          $(this).find('.foldingMenu a').css({
            'font-size': '15px'
          });
          $(this).find('.titleDescription').css({
            'visibility': 'visible',
            'opacity': '1'
          });
          $(this).find('.titleBox').css({
            'color' : '#F24F00',
          });
          $(this).css({
            'border': '1px solid #F24F00',
            'background': '#222222',
            'height': '290px'
          });
          marketingOpen = true;
          if ($(window).height() <= 620) {

            $('.infoContainer').css({
              'bottom': '-10%'
            });
            $('.bottom').css({
              'bottom': '-11%'
            });
            $('.homeWrapper').css({
              'height': '111%'
            });

          }
        }

        if (techOpen){
          $('#titleTech').find('.foldingMenu').css({
            'visibility': 'hidden',
            'opacity': '0'
          });
          $('#titleTech').find('.foldingMenu li').css({
            'height': '0px'
          });
          $('#titleTech').find('.foldingMenu a').css({
            'font-size': '0px'
          });
          $('#titleTech').find('.titleDescription').css({
            'visibility': 'hidden',
            'opacity': '0'
          });
          $('#titleTech').find('.titleBox').css({
            'color' : 'white',
          });
          $('#titleTech').css({
            'border': '1px solid #F24F00',
            'background': 'rgba(255, 255, 255, 0)',
            'height': '54px'
          });
          techOpen = false;
        }
      if (entertainment){
        $('#titleEntertainment').find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleEntertainment').find('.foldingMenu li').css({
          'height': '0px'
        });
        $('#titleEntertainment').find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $('#titleEntertainment').find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleEntertainment').find('.titleBox').css({
          'color' : 'white',
        });
        $('#titleEntertainment').css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        entertainment = false;
      }

    });
    $('#titleTech').click(function() {
      if (techOpen) {
        $(this).find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $(this).find('.foldingMenu li').css({
          'height': '0px'
        });
        $(this).find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $(this).find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $(this).find('.titleBox').css({
          'color' : 'white',
        });
        $(this).css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        techOpen = false;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '1%'
          });
          $('.bottom').css({
            'bottom': '0'
          });
          $('.homeWrapper').css({
            'height': '100%'
          });

        }
      }else {
        $(this).find('.foldingMenu').css({
          'visibility': 'visible',
          'opacity': '1'
        });
        $(this).find('.foldingMenu li').css({
          'height': '30px'
        });
        $(this).find('.foldingMenu a').css({
          'font-size': '15px'
        });
        $(this).find('.titleDescription').css({
          'visibility': 'visible',
          'opacity': '1'
        });
        $(this).find('.titleBox').css({
          'color' : '#F24F00',
        });
        $(this).css({
          'border': '1px solid #F24F00',
          'background': '#222222',
          'height': '290px'
        });
        techOpen = true;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '-10%'
          });
          $('.bottom').css({
            'bottom': '-11%'
          });
          $('.homeWrapper').css({
            'height': '111%'
          });

        }
      }

      if (marketingOpen){
        $('#titleMarketing').find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleMarketing').find('.foldingMenu li').css({
          'height': '0px'
        });
        $('#titleMarketing').find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $('#titleMarketing').find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleMarketing').find('.titleBox').css({
          'color' : 'white',
        });
        $('#titleMarketing').css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        marketingOpen = false;
      }
      if (entertainment){
        $('#titleEntertainment').find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleEntertainment').find('.foldingMenu li').css({
          'height': '0px'
        });
        $('#titleEntertainment').find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $('#titleEntertainment').find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleEntertainment').find('.titleBox').css({
          'color' : 'white',
        });
        $('#titleEntertainment').css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        entertainment = false;
      }

    });
    $('#titleEntertainment').click(function() {
      if (entertainment) {

        $(this).find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $(this).find('.foldingMenu li').css({
          'height': '0px'
        });
        $(this).find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $(this).find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $(this).find('.titleBox').css({
          'color' : 'white',
        });
        $(this).css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        entertainment = false;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '1%'
          });
          $('.bottom').css({
            'bottom': '0'
          });
          $('.homeWrapper').css({
            'height': '100%'
          });


        }
      }else {

        $(this).find('.foldingMenu').css({
          'visibility': 'visible',
          'opacity': '1'
        });
        $(this).find('.foldingMenu li').css({
          'height': '30px'
        });
        $(this).find('.foldingMenu a').css({
          'font-size': '15px'
        });
        $(this).find('.titleDescription').css({
          'visibility': 'visible',
          'opacity': '1'
        });
        $(this).find('.titleBox').css({
          'color' : '#F24F00',
        });
        $(this).css({
          'border': '1px solid #F24F00',
          'background': '#222222',
          'height': '290px'
        });
        entertainment = true;
        if ($(window).height() <= 620) {

          $('.infoContainer').css({
            'bottom': '-10%'
          });
          $('.bottom').css({
            'bottom': '-11%'
          });
          $('.homeWrapper').css({
            'height': '111%'
          });

        }
      }

      if (marketingOpen){
        $('#titleMarketing').find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleMarketing').find('.foldingMenu li').css({
          'height': '0px'
        });
        $('#titleMarketing').find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $('#titleMarketing').find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleMarketing').find('.titleBox').css({
          'color' : 'white',
        });
        $('#titleMarketing').css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        marketingOpen = false;
      }
      if (techOpen){
        $('#titleTech').find('.foldingMenu').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleTech').find('.foldingMenu li').css({
          'height': '0px'
        });
        $('#titleTech').find('.foldingMenu a').css({
          'font-size': '0px'
        });
        $('#titleTech').find('.titleDescription').css({
          'visibility': 'hidden',
          'opacity': '0'
        });
        $('#titleTech').find('.titleBox').css({
          'color' : 'white',
        });
        $('#titleTech').css({
          'border': '1px solid #F24F00',
          'background': 'rgba(255, 255, 255, 0)',
          'height': '54px'
        });
        techOpen = false;
      }

    });


  }




});
