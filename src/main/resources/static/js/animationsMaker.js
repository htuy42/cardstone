function killAnimBox(){
    let box = $(".animBox");
    box.empty();
    box.hide();
}

function getDrawnCardAnimation(isMe){
    let anim = new cardDrawnAnimation();
    anim.setTarget(isMe);
    return anim;
}

class animationsMaker{	

    static playCardAnimation(card){
        cardCache.repairCard(card);
        eventQueue.add(card.id);
        let box = $(".animBox");
        box.empty();
        let cardFromCache = cardCache.getByIID(card.id);
        box.append("<div class='cardBox'></div>");
        cardFromCache.drawGivenSpace(box.find(".cardBox"));
        box.show();
        window.setTimeout(killAnimBox,1500);
        box.click(killAnimBox);
    }
    


	static getAttackAnimation(attacker,defender){
		let anim = new linearAnimation();
		let attackerCard = cardCache.getByIID(attacker);
		anim.setColorSet(attackerCard.cost.manaPool.getColors());
		anim.setShape("circle");
		anim.setRadius(1);
		anim.setCount(150);
		anim.setSpeed(.5);
		anim.setRandom(true);
		anim.setStartDivById(attacker);
		anim.setEndDivById(defender);
		return anim;
	}
	
	static getDamagedAnimation(damaged){
		let anim = new singleRadial();
		anim.setColor("red");
		anim.setShape("circle");
		anim.setRadius(50);
		anim.setSpeed(.3);
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
	}
    
    static getDeadAnimation(damaged){
        let anim = new singleRadial();
		anim.setColor("black");
		anim.setShape("circle");
		anim.setRadius(50);
		anim.setSpeed(.3);
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
    }
    
    static getPlayerDamagedAnimation(isMe){
        let anim = new singleRadial();
		anim.setColor("red");
		anim.setShape("circle");
		anim.setRadius(50);
		anim.setSpeed(.3);
        if(isMe){
            anim.setCenterDiv($(".overlayHUD1"));
        }
        else{
            anim.setCenterDiv($(".overlayHUD2"));
        }
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
    }
    
    static getPlayerAttackedAnimation(attacker,isMe){
        let anim = new linearAnimation();
		let attackerCard = cardCache.getByIID(attacker);
		anim.setColorSet(attackerCard.cost.manaPool.getColors());
		anim.setShape("circle");
		anim.setRadius(1);
		anim.setCount(150);
		anim.setSpeed(.5);
		anim.setRandom(true);
		anim.setStartDivById(attacker);
		if(isMe){
            anim.setEndDiv($(".overlayHUD1"));
        }
        else{
            anim.setEndDiv($(".overlayHUD2"));
        }
		return anim;
    }
}

const CARD_DRAW_ANIM_HEIGHT_OFFSET = 80;
const CARD_DRAW_ANIM_HEIGHT = 140;

class cardDrawnAnimation extends animation{
    
    constructor(){
		super();
        let off = $(".centerClockRect").offset(); 
        this.setStartLoc(off.left,off.top);
        this.speed = .6;
	}
    
    create(){
        let drawables = [];
        let drawable = new movingDrawable("card",this.startX,this.startY,this.goalX,this.goalY,"red",this.speed,5);
        drawables.push(drawable);
        return drawables;
    }
    setStartLoc(startX, startY){
        this.startX = startX;
        this.startY = startY;
    }
    setTarget(isMe){
        let wind = $(window);
        this.goalX = wind.width() / 2;
        if(isMe){
            this.goalY = wind.height() - CARD_DRAW_ANIM_HEIGHT_OFFSET;
        }
        else{
            this.goalY = 0;
        }
    }
}