<!DOCTYPE html>
	<head>
		<link rel="stylesheet" href="css/resZone.css">
		<link rel="stylesheet" href="css/bootstrap.css">
		<link rel="stylesheet" href="css/cardBack.css">
		<link rel="stylesheet" href="css/newBoard.css">
		<link rel="stylesheet" href="css/cardStyle.css">
		<link rel="stylesheet" href="css/smallCard.css">
		<link rel="stylesheet" href="css/bigCard.css">
		<link rel="stylesheet" href="css/tinyCard.css">
		<link rel="stylesheet" href="qtip.css">
	</head>
	<body>
	<div class="modal fade" id="endTurnAsk" tabindex="-1" role="dialog" aria-hidden="true">
	  <div class="modal-dialog alert" role="document">
		<div class="modal-content">
		  <div class="modal-header">
			<h4 class="modal-title">Really end your turn?</h4>
		  </div>
		  <div class="modal-body">
			<br>
			<button type="submit" class="btn btn-primary endTurnButton">Yes</button>
			<button type="cancel" class="btn btn-secondary pull-right" data-dismiss="modal">No</button>
			<br>
		  </div>
		</div>
	  </div>
	</div>
    <div class="modal fade" id="optionsMenu" tabindex="-1" role="dialog" aria-hidden="true">
	  <div class="modal-dialog alert" role="document">
		<div class="modal-content">
		  <div class="modal-header">
			<h4 class="modal-title">Options!</h4>
		  </div>
		  <div class="modal-body">
			<input type="checkbox" id="tooltipsToggle">Display tooltips on hover<br>
		  </div>
          <div class="modal-footer">
            <button type="cancel" class="btn btn-primary" data-dismiss="modal">Return to the game!</button>
          </div>     
		</div>
	  </div>
	</div>
	<div class="modal fade bigModal" id="chooseOneAsk" tabindex="-1" role="dialog" aria-hidden="true">
	  <div class="modal-dialog alert" role="document">
		<div class="modal-content">
            <div class="modal-header center-text">
                <h4 class="modal-title blue-font">Choose One!</h4>
              </div>
		  <div class="modal-body" id="chooseZoneDisplay">

		  </div>
		</div>
	  </div>
	</div>
	<canvas class="boardOverlay" ></canvas>
    <div class="overlayHand1 HUD"></div>
    <div class="overlayHand2 HUD"></div>
    <div class="alert chat HUD" id="chatBox">
    <div class="chatLog"></div>
    <div class="chatMessageType">
        <textarea id="chatMessageContent" type="text" placeholder="message"></textarea>
        <button class="pull-right" id="chatMessageSubmit">Submit</button>
    </div>
    </div>
    <div id="manaDisplay1" class="manaHUD1 HUD">
        <div class="zone mana manaPool resDisplay" id="mana1"></div>
    </div>
    <div id="manaDisplay2" class="manaHUD2 HUD">
        <div class="zone mana manaPool resDisplay" id="mana2"></div>
    </div>
    <div class="overlayHUD1 HUD hasTooltip resHUD">
        <div class="healthBox health resDisplay" id="health1"></div>
        <div class="deck deckBox resDisplay" id="deck1"></div>
        <div class="healthLine"></div>
        <div class="resBox regRes resDisplay" id="regRes1"></div>
        
        <div class="userIcon"><div class="optionsGear" id="optionsPopperGear"></div></div>
    </div>
    <div class="overlayHUD2 HUD hasTooltip resHUD">
        <div class="healthBox health resDisplay" id="health2"></div>
        <div class="deck deckBox resDisplay" id="deck2"></div>
        <div class="healthLine"></div>
        <div class="resBox regRes resDisplay" id="regRes2"></div>
        <div class="userIcon"></div>
    </div>
        <div class="boxOuter" onmousedown="return false">
            <div class="boxInner">
                <div class="board">
                    <div class="onTop" id="onTop2"></div>
                    <div class="onTop" id="onTop1"></div>
                    <div class="zone centerClockRect centerInfo endTurnButton"></div>
                    <div class="lineLeft"></div>
                    <div class="lineRight"></div>
                    <div class="lineLeftLight"></div>
                    <div class="lineRightLight"></div>
                    <div class="boardSide side2">
                        <div class="zone playerArea player2">
                            <div class="creature2 creature"></div>
                            <div class="aura2 aura"></div>
                        </div>
                    </div>
                    <div class="boardSide side1">
                        <div class="zone playerArea player1">
                            <div class="creature1 creature"></div>
                            <div class="aura1 aura"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

		<script src="js/jquery-2.1.1.js"></script>
		<script src="js/jquery-cookie.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
        <script src="js/MouseManagerSystem.js"></script>
		<script src="js/animationsMaker.js"></script>
		<script src="js/Enums.js"></script>
        <script src="js/TurnTimer.js"></script>
		<script src="js/tether.min.js"></script>
		<script src="js/bootstrap.js"></script>
		<script src="js/cardCacher.js"></script>
		<script src="js/drawable.js"></script>
		<script src="js/animation.js"></script>
		<script src="js/radialAnimation.js"></script>
		<script src="js/movingDrawable.js"></script>
		<script src="js/linearAnimation.js"></script>
		<script src="js/manaPool.js"></script>
		<script src="js/cost.js"></script>
		<script src="js/card.js"></script>
		<script src="js/drawableZone.js"></script>
        <script src="js/hudZone.js"></script>
		<script src="js/healthResZone.js"></script>
		<script src="js/chooseZone.js"></script>
		<script src="js/cardCollection.js"></script>
		<script src="js/board.js"></script>
		<script src="js/server.js"></script>
		<script src="js/cardDrawing.js"></script>
		<script src="js/qtip.js"></script>
	</body>
</html>